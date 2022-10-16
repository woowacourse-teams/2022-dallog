import { validateLength } from '@/validation';
import { useTheme } from '@emotion/react';
import { useState } from 'react';

import { useGetEditableCategories } from '@/hooks/@queries/category';
import { usePatchSchedule } from '@/hooks/@queries/schedule';
import useControlledInput from '@/hooks/useControlledInput';
import useValidateSchedule from '@/hooks/useValidateSchedule';

import { ScheduleType } from '@/@types/schedule';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import Select from '@/components/@common/Select/Select';
import SelectWithId from '@/components/@common/SelectWithId/SelectWithId';

import { CATEGORY_TYPE } from '@/constants/category';
import { DATE_TIME, TIMES } from '@/constants/date';
import { VALIDATION_MESSAGE, VALIDATION_SIZE } from '@/constants/validate';

import {
  checkAllDay,
  getDayOffsetDateTime,
  getISODateString,
  getISOTimeString,
} from '@/utils/date';

import {
  arrowStyle,
  cancelButtonStyle,
  categoryBoxStyle,
  checkboxStyle,
  controlButtonsStyle,
  dateFieldsetStyle,
  dateTimePickerStyle,
  dateTimeStyle,
  formStyle,
  labelStyle,
  modalStyle,
  saveButtonStyle,
  selectTimeStyle,
} from './ScheduleModifyModal.styles';

interface ScheduleModifyModalProps {
  scheduleInfo: ScheduleType;
  closeModal: () => void;
}

function ScheduleModifyModal({ scheduleInfo, closeModal }: ScheduleModifyModalProps) {
  const theme = useTheme();

  const [isAllDay, setAllDay] = useState(
    checkAllDay(scheduleInfo.startDateTime, scheduleInfo.endDateTime)
  );

  const { data } = useGetEditableCategories({});

  const { mutate } = usePatchSchedule({
    scheduleId: scheduleInfo.id,
    onSuccess: () => closeModal(),
  });

  const categoryId = useControlledInput(
    String(data?.data.find((category) => category.id === scheduleInfo.categoryId)?.id)
  );

  const validationSchedule = useValidateSchedule({
    initialTitle: scheduleInfo.title,
    initialStartDate: getISODateString(scheduleInfo.startDateTime),
    initialStartTime: getISOTimeString(scheduleInfo.startDateTime).slice(0, 5),
    initialEndDate: getISODateString(
      isAllDay ? getDayOffsetDateTime(scheduleInfo.endDateTime, -1) : scheduleInfo.endDateTime
    ),
    initialEndTime: getISOTimeString(scheduleInfo.endDateTime).slice(0, 5),
    initialMemo: scheduleInfo.memo,
  });

  const handleSubmitScheduleModifyForm = (e: React.FormEvent) => {
    e.preventDefault();

    const body = {
      title: validationSchedule.title.inputValue,
      startDateTime: `${validationSchedule.startDate.inputValue}T${
        isAllDay ? DATE_TIME.START : validationSchedule.startTime.inputValue
      }`,
      endDateTime: `${
        isAllDay
          ? `${getISODateString(getDayOffsetDateTime(validationSchedule.endDate.inputValue, 1))}T${
              DATE_TIME.END
            }`
          : `${validationSchedule.endDate.inputValue}T${validationSchedule.endTime.inputValue}`
      }`,
      memo: validationSchedule.memo.inputValue,
      categoryId:
        data?.data.find((category) => category.id === Number(categoryId.inputValue))?.id ||
        scheduleInfo.categoryId,
    };

    mutate(body);
  };

  const handleClickAllDayButton = () => {
    setAllDay((prev) => !prev);
  };

  const categories = data?.data
    .filter((category) => category.categoryType !== CATEGORY_TYPE.GOOGLE)
    .map((category) => {
      return {
        id: category.id,
        name: category.name,
      };
    });

  return (
    <div css={modalStyle}>
      <form css={formStyle} onSubmit={handleSubmitScheduleModifyForm}>
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
          labelText="제목"
        />
        <div css={dateTimeStyle}>
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

          <p css={arrowStyle}>↓</p>
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
        {categories && (
          <div css={categoryBoxStyle}>
            <div css={labelStyle}>카테고리</div>
            <SelectWithId
              options={categories}
              value={categoryId.inputValue}
              onChange={categoryId.onChangeValue}
            />
          </div>
        )}
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
        <div css={controlButtonsStyle}>
          <Button cssProp={cancelButtonStyle(theme)} onClick={closeModal}>
            취소
          </Button>
          <Button
            type="submit"
            cssProp={saveButtonStyle(theme)}
            disabled={!validationSchedule.isValidSchedule}
          >
            저장
          </Button>
        </div>
      </form>
    </div>
  );
}

export default ScheduleModifyModal;
