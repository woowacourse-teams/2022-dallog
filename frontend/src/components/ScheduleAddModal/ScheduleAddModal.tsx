import { validateLength } from '@/validation';
import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useValidateSchedule from '@/hooks/useValidateSchedule';

import { CalendarType } from '@/@types/calendar';
import { CategoryType } from '@/@types/category';
import { ScheduleType } from '@/@types/schedule';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';

import { CACHE_KEY, VALIDATION_SIZE } from '@/constants';
import { DATE_TIME } from '@/constants/date';
import { VALIDATION_MESSAGE } from '@/constants/message';

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
  const { accessToken } = useRecoilValue(userState);
  const theme = useTheme();

  const [categoryId, setCategoryId] = useState(0);
  const [isAllDay, setAllDay] = useState(true);

  const queryClient = useQueryClient();

  const { data } = useQuery<AxiosResponse<CategoryType[]>, AxiosError>(
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
    Omit<ScheduleType, 'id' | 'categoryId' | 'colorCode'>,
    unknown
  >((body) => scheduleApi.post(accessToken, categoryId, body), {
    onSuccess: () => {
      onSuccessPostSchedule();
    },
  });

  const dateFieldset = isAllDay
    ? {
        type: 'date',
        defaultValue: getDate(dateInfo),
      }
    : {
        type: 'datetime-local',
        defaultValue: getDateTime(dateInfo),
      };

  const validationSchedule = useValidateSchedule({
    defaultStartDateTime: dateFieldset.defaultValue,
    defaultEndDateTime: dateFieldset.defaultValue,
  });

  const handleClickAllDayButton = () => {
    setAllDay((prev) => !prev);
  };

  const handleSubmitScheduleAddForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const body = {
      title: validationSchedule.title.inputValue,
      startDateTime: validationSchedule.startDateTime.inputValue,
      endDateTime: validationSchedule.endDateTime.inputValue,
      memo: validationSchedule.memo.inputValue,
    };

    if (!isAllDay) {
      postSchedule(body);

      return;
    }

    const allDayBody = {
      ...body,
      startDateTime: `${body.startDateTime}T${DATE_TIME.START}`,
      endDateTime: `${body.endDateTime}T${DATE_TIME.END}`,
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
          {data?.data.map((category) => (
            <option key={category.id} value={category.id}>
              {category.name}
            </option>
          ))}
        </select>
        <Fieldset
          placeholder="제목을 입력하세요."
          onChange={validationSchedule.title.onChange}
          isValid={validateLength(
            validationSchedule.title.inputValue,
            VALIDATION_SIZE.MIN_LENGTH,
            VALIDATION_SIZE.SCHEDULE_TITLE_MAX_LENGTH
          )}
          errorMessage={VALIDATION_MESSAGE.STRING_LENGTH(
            VALIDATION_SIZE.MIN_LENGTH,
            VALIDATION_SIZE.SCHEDULE_TITLE_MAX_LENGTH
          )}
          autoFocus
        />
        <Button cssProp={allDayButton(theme, isAllDay)} onClick={handleClickAllDayButton}>
          종일
        </Button>
        <div css={dateTime} key={dateFieldset.type}>
          <Fieldset
            type={dateFieldset.type}
            defaultValue={dateFieldset.defaultValue}
            onChange={validationSchedule.startDateTime.onChange}
          />
          <p css={arrow}>↓</p>
          <Fieldset
            type={dateFieldset.type}
            defaultValue={dateFieldset.defaultValue}
            onChange={validationSchedule.endDateTime.onChange}
          />
        </div>
        <Fieldset
          placeholder="메모를 추가하세요."
          onChange={validationSchedule.memo.onChange}
          isValid={validateLength(
            validationSchedule.memo.inputValue,
            VALIDATION_SIZE.MIN_LENGTH,
            VALIDATION_SIZE.SCHEDULE_MEMO_MAX_LENGTH
          )}
          errorMessage={VALIDATION_MESSAGE.STRING_LENGTH(
            VALIDATION_SIZE.MIN_LENGTH,
            VALIDATION_SIZE.SCHEDULE_MEMO_MAX_LENGTH
          )}
        />
        <div css={controlButtons}>
          <Button cssProp={cancelButton(theme)} onClick={closeModal}>
            취소
          </Button>
          <Button
            type="submit"
            cssProp={saveButton(theme)}
            disabled={!validationSchedule.isValidSchedule}
          >
            저장
          </Button>
        </div>
      </form>
    </div>
  );
}

export default ScheduleAddModal;
