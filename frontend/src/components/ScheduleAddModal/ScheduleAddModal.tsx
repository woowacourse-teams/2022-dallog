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

import { CACHE_KEY } from '@/constants';
import { DATE_TIME } from '@/constants/date';
import { VALIDATION_MESSAGE, VALIDATION_SIZE } from '@/constants/validate';

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
    Omit<ScheduleType, 'id' | 'categoryId' | 'colorCode' | 'categoryType'>,
    unknown
  >((body) => scheduleApi.post(accessToken, categoryId, body), {
    onSuccess: () => {
      onSuccessPostSchedule();
    },
  });

  const dateFieldset = isAllDay
    ? {
        type: 'date',
        initialValue: getDate(dateInfo),
      }
    : {
        type: 'datetime-local',
        initialValue: getDateTime(dateInfo),
      };

  const validationSchedule = useValidateSchedule({
    initialStartDateTime: dateFieldset.initialValue,
    initialEndDateTime: dateFieldset.initialValue,
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
          value={validationSchedule.title.inputValue}
          onChange={validationSchedule.title.onChangeValue}
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
            value={validationSchedule.startDateTime.inputValue}
            onChange={validationSchedule.startDateTime.onChangeValue}
          />
          <p css={arrow}>↓</p>
          <Fieldset
            type={dateFieldset.type}
            value={validationSchedule.endDateTime.inputValue}
            onChange={validationSchedule.endDateTime.onChangeValue}
          />
        </div>
        <Fieldset
          placeholder="메모를 추가하세요."
          value={validationSchedule.memo.inputValue}
          onChange={validationSchedule.memo.onChangeValue}
          isValid={validateLength(
            validationSchedule.memo.inputValue,
            0,
            VALIDATION_SIZE.SCHEDULE_MEMO_MAX_LENGTH
          )}
          errorMessage={VALIDATION_MESSAGE.STRING_LENGTH(
            0,
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
