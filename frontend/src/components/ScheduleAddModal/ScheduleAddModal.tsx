import { validateLength } from '@/validation';
import { useTheme } from '@emotion/react';
import { useState } from 'react';

import { useGetEditableCategories } from '@/hooks/@queries/category';
import { usePostSchedule } from '@/hooks/@queries/schedule';
import useControlledInput from '@/hooks/useControlledInput';
import useValidateSchedule from '@/hooks/useValidateSchedule';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import Select from '@/components/@common/Select/Select';
import SelectWithId from '@/components/@common/SelectWithId/SelectWithId';
import Spinner from '@/components/@common/Spinner/Spinner';

import { DATE_TIME, TIMES } from '@/constants/date';
import { VALIDATION_MESSAGE, VALIDATION_SIZE } from '@/constants/validate';

import { getDayOffsetDateTime, getEndTime, getISODateString, getStartTime } from '@/utils/date';

import {
  arrow,
  cancelButton,
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
  selectTimeStyle,
} from './ScheduleAddModal.styles';

interface ScheduleAddModalProps {
  dateInfo: string;
  closeModal: () => void;
}

function ScheduleAddModal({ dateInfo, closeModal }: ScheduleAddModalProps) {
  const theme = useTheme();

  const [isAllDay, setAllDay] = useState(true);

  const { isLoading, data } = useGetEditableCategories({});

  const categoryId = useControlledInput(String(data?.data[0].id));

  const { mutate: postSchedule } = usePostSchedule({
    categoryId: categoryId.inputValue,
    onSuccess: () => closeModal(),
  });

  const validationSchedule = useValidateSchedule({
    initialStartDate: getISODateString(dateInfo),
    initialStartTime: isAllDay ? DATE_TIME.START : getStartTime(),
    initialEndDate: getISODateString(dateInfo),
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
          ? getISODateString(getDayOffsetDateTime(validationSchedule.endDate.inputValue, 1))
          : validationSchedule.endDate.inputValue
      }T${validationSchedule.endTime.inputValue}`,
      memo: validationSchedule.memo.inputValue,
    };

    postSchedule(body);
  };

  if (isLoading || data === undefined) {
    return <Spinner size={10} />;
  }

  const categories = data.data.map((category) => {
    return {
      id: category.id,
      name: category.name,
    };
  });

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
              readOnly
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
                cssProp={selectTimeStyle}
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
                cssProp={selectTimeStyle}
              />
            )}
          </div>
        </div>
        <div css={selectBoxStyle}>
          <span css={labelStyle}>카테고리</span>
          <SelectWithId
            options={categories}
            value={categoryId.inputValue}
            onChange={categoryId.onChangeValue}
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
