import { validateLength } from '@/validation';
import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useControlledInput from '@/hooks/useControlledInput';
import useValidateSchedule from '@/hooks/useValidateSchedule';

import { CalendarType } from '@/@types/calendar';
import { CategoryType } from '@/@types/category';
import { ScheduleType } from '@/@types/schedule';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import Select from '@/components/@common/Select/Select';
import Spinner from '@/components/@common/Spinner/Spinner';

import { CACHE_KEY } from '@/constants/api';
import { DATE_TIME, TIMES } from '@/constants/date';
import { VALIDATION_MESSAGE, VALIDATION_SIZE } from '@/constants/validate';

import { getDate, getEndTime, getISODateString, getNextDate, getStartTime } from '@/utils/date';

import categoryApi from '@/api/category';
import scheduleApi from '@/api/schedule';

import {
  arrow,
  cancelButton,
  categorySelect,
  checkboxStyle,
  controlButtons,
  dateFieldsetStyle,
  dateTime,
  dateTimePickerStyle,
  form,
  labelStyle,
  saveButton,
  scheduleAddModal,
  selectBoxStyle,
} from './ScheduleAddModal.styles';

interface ScheduleAddModalProps {
  dateInfo: Omit<CalendarType, 'day'>;
  closeModal: () => void;
}

function ScheduleAddModal({ dateInfo, closeModal }: ScheduleAddModalProps) {
  const { accessToken } = useRecoilValue(userState);

  const theme = useTheme();

  const [isAllDay, setAllDay] = useState(true);

  const queryClient = useQueryClient();

  const { isLoading: isGetCategoryLoading, data } = useQuery<
    AxiosResponse<CategoryType[]>,
    AxiosError
  >(CACHE_KEY.MY_CATEGORIES, () => categoryApi.getMy(accessToken));

  const categoryId = useControlledInput(String(data?.data[0].id));

  const { mutate: postSchedule } = useMutation<
    AxiosResponse<{ schedules: ScheduleType[] }>,
    AxiosError,
    Omit<ScheduleType, 'id' | 'categoryId' | 'colorCode' | 'categoryType'>,
    unknown
  >((body) => scheduleApi.post(accessToken, Number(categoryId.inputValue), body), {
    onSuccess: () => {
      onSuccessPostSchedule();
    },
  });

  const validationSchedule = useValidateSchedule({
    initialStartDate: getDate(dateInfo),
    initialStartTime: isAllDay ? DATE_TIME.START : getStartTime(),
    initialEndDate: getDate(dateInfo),
    initialEndTime: isAllDay ? DATE_TIME.END : getEndTime(),
  });

  const handleClickAllDayButton = () => {
    setAllDay((prev) => !prev);
  };

  const handleSubmitScheduleAddForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const body = {
      title: validationSchedule.title.inputValue,
      startDateTime: `${validationSchedule.startDate.inputValue}T${validationSchedule.startTime.inputValue}`,
      endDateTime: `${
        isAllDay
          ? getISODateString(
              getNextDate(new Date(validationSchedule.endDate.inputValue), 1).toISOString()
            )
          : validationSchedule.endDate.inputValue
      }T${validationSchedule.endTime.inputValue}`,
      memo: validationSchedule.memo.inputValue,
    };

    postSchedule(body);
  };

  const onSuccessPostSchedule = () => {
    queryClient.invalidateQueries(CACHE_KEY.SCHEDULES);

    closeModal();
  };

  if (isGetCategoryLoading || data === undefined) {
    return <Spinner size={10} />;
  }

  return (
    <div css={scheduleAddModal}>
      <form css={form} onSubmit={handleSubmitScheduleAddForm}>
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
          labelText="제목"
        />
        <div css={dateTime}>
          <div css={checkboxStyle}>
            <input
              type="checkbox"
              id="allDay"
              checked={isAllDay}
              onClick={handleClickAllDayButton}
            />
            <label htmlFor="allDay" />
            <label htmlFor="allDay">종일</label>
          </div>
          <div css={dateTimePickerStyle}>
            <Fieldset
              type="date"
              value={validationSchedule.startDate.inputValue}
              onChange={validationSchedule.startDate.onChangeValue}
              labelText={isAllDay ? '날짜' : '날짜 / 시간'}
              cssProp={dateFieldsetStyle(isAllDay)}
            />
            {!isAllDay && (
              <Select
                options={TIMES}
                value={validationSchedule.startTime.inputValue}
                onChange={validationSchedule.startTime.onChangeValue}
              />
            )}
          </div>
          <p css={arrow}>↓</p>
          <div css={dateTimePickerStyle}>
            <Fieldset
              type="date"
              value={validationSchedule.endDate.inputValue}
              onChange={validationSchedule.endDate.onChangeValue}
              cssProp={dateFieldsetStyle(isAllDay)}
              min={validationSchedule.startDate.inputValue}
            />
            {!isAllDay && (
              <Select
                options={TIMES}
                value={validationSchedule.endTime.inputValue}
                onChange={validationSchedule.endTime.onChangeValue}
              />
            )}
          </div>
        </div>
        <div css={selectBoxStyle}>
          <span css={labelStyle}>카테고리</span>
          <select
            css={categorySelect}
            value={categoryId.inputValue}
            onChange={categoryId.onChangeValue}
          >
            {data?.data.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </select>
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
          labelText="메모 (선택)"
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
