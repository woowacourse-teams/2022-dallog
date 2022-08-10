import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useRef, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import { CalendarType } from '@/@types/calendar';
import { CategoryType } from '@/@types/category';
import { ScheduleType } from '@/@types/schedule';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';

import { CACHE_KEY } from '@/constants';

import { createPostBody } from '@/utils';
import { getDate, getDateTime } from '@/utils/date';

import categoryApi from '@/api/category';
import scheduleApi from '@/api/schedule';

import {
  allDayButton,
  arrow,
  cancelButton,
  categorySelect,
  controlButtons,
  dateTime,
  form,
  saveButton,
  scheduleAddModal,
} from './ScheduleAddModal.styles';

interface ScheduleAddModalProps {
  dateInfo: CalendarType | null;
  closeModal: () => void;
}

function ScheduleAddModal({ dateInfo, closeModal }: ScheduleAddModalProps) {
  const [categoryId, setCategoryId] = useState(0);
  const [isAllDay, setAllDay] = useState(true);
  const theme = useTheme();

  const { accessToken } = useRecoilValue(userState);

  const queryClient = useQueryClient();

  const { data: myCategoriesGetResponse } = useQuery<AxiosResponse<CategoryType[]>, AxiosError>(
    CACHE_KEY.MY_CATEGORIES,
    () => categoryApi.getMy(accessToken)
  );

  const {
    isLoading,
    error,
    mutate: postSchedule,
  } = useMutation<
    AxiosResponse<{ schedules: ScheduleType[] }>,
    AxiosError,
    Omit<ScheduleType, 'id'>,
    unknown
  >((body) => scheduleApi.post(accessToken, categoryId, body), {
    onSuccess: () => {
      onSuccessPostSchedule();
    },
  });

  const handleClickAllDayButton = () => {
    setAllDay((prev) => !prev);
  };

  const inputRef = {
    title: useRef<HTMLInputElement>(null),
    startDateTime: useRef<HTMLInputElement>(null),
    endDateTime: useRef<HTMLInputElement>(null),
    memo: useRef<HTMLInputElement>(null),
  };

  const handleSubmitScheduleAddForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const body = createPostBody(inputRef);

    if (!body) {
      return;
    }

    if (!isAllDay) {
      postSchedule(body);

      return;
    }

    const allDayBody = {
      ...body,
      startDateTime: `${body.startDateTime}T00:00`,
      endDateTime: `${body.endDateTime}T23:59`,
    };

    postSchedule(allDayBody);
  };

  const handleChangeMyCategorySelect = ({ target }: React.ChangeEvent<HTMLSelectElement>) => {
    const categoryId = Number(target.value);

    setCategoryId(categoryId);
  };

  const onSuccessPostSchedule = () => {
    queryClient.invalidateQueries(CACHE_KEY.SCHEDULES);
    closeModal();
  };

  if (isLoading) return <>Loading</>;

  if (error) return <>Error</>;

  const dateFieldset = isAllDay
    ? {
        type: 'date',
        defaultValue: getDate(dateInfo),
      }
    : {
        type: 'datetime-local',
        defaultValue: getDateTime(dateInfo),
      };

  return (
    <div css={scheduleAddModal}>
      <form css={form} onSubmit={handleSubmitScheduleAddForm}>
        <select
          id="myCategories"
          defaultValue=""
          css={categorySelect}
          onChange={handleChangeMyCategorySelect}
        >
          <option value="" disabled>
            카테고리
          </option>
          {myCategoriesGetResponse?.data.map((category) => (
            <option key={category.id} value={category.id}>
              {category.name}
            </option>
          ))}
        </select>
        <Fieldset placeholder="제목을 입력하세요." refProp={inputRef.title} />
        <Button cssProp={allDayButton(theme, isAllDay)} onClick={handleClickAllDayButton}>
          종일
        </Button>
        <div css={dateTime} key={dateFieldset.type}>
          <Fieldset
            type={dateFieldset.type}
            defaultValue={dateFieldset.defaultValue}
            refProp={inputRef.startDateTime}
          />
          <p css={arrow}>↓</p>
          <Fieldset
            type={dateFieldset.type}
            defaultValue={dateFieldset.defaultValue}
            refProp={inputRef.endDateTime}
          />
        </div>
        <Fieldset placeholder="메모를 추가하세요." refProp={inputRef.memo} />
        <div css={controlButtons}>
          <Button cssProp={cancelButton(theme)} onClick={closeModal}>
            취소
          </Button>
          <Button type="submit" cssProp={saveButton(theme)}>
            저장
          </Button>
        </div>
      </form>
    </div>
  );
}

export default ScheduleAddModal;
