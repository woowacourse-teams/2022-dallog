import { validateLength } from '@/validation';
import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useValidateSchedule from '@/hooks/useValidateSchedule';

import { CategoryType } from '@/@types/category';
import { ScheduleType } from '@/@types/schedule';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';

import { CACHE_KEY, VALIDATION_SIZE } from '@/constants';
import { VALIDATION_MESSAGE } from '@/constants/message';

import { checkAllDay, getISODateString } from '@/utils/date';

import categoryApi from '@/api/category';
import scheduleApi from '@/api/schedule';

import {
  allDayButtonStyle,
  arrowStyle,
  cancelButtonStyle,
  categoryStyle,
  controlButtonsStyle,
  dateTimeStyle,
  formStyle,
  modalStyle,
  saveButtonStyle,
} from './ScheduleModifyModal.styles';

interface ScheduleModifyModalProps {
  scheduleInfo: ScheduleType;
  closeModal: () => void;
}

function ScheduleModifyModal({ scheduleInfo, closeModal }: ScheduleModifyModalProps) {
  const { accessToken } = useRecoilValue(userState);
  const theme = useTheme();

  const [isAllDay, setAllDay] = useState(
    !!checkAllDay(scheduleInfo.startDateTime, scheduleInfo.endDateTime)
  );

  const queryClient = useQueryClient();

  const { data } = useQuery<AxiosResponse<CategoryType>, AxiosError>(
    CACHE_KEY.CATEGORY,
    () => categoryApi.getSingle(scheduleInfo.categoryId),
    { useErrorBoundary: true }
  );

  const { mutate } = useMutation<
    AxiosResponse,
    AxiosError,
    Omit<ScheduleType, 'id' | 'categoryId' | 'colorCode'>,
    unknown
  >(CACHE_KEY.SCHEDULE, (body) => scheduleApi.patch(accessToken, scheduleInfo.id, body), {
    onSuccess: () => onSuccessPatchSchedule(),
    useErrorBoundary: true,
  });

  const onSuccessPatchSchedule = () => {
    queryClient.invalidateQueries(CACHE_KEY.SCHEDULES);
    closeModal();
  };

  const validationSchedule = useValidateSchedule({
    defaultTitle: scheduleInfo.title,
    defaultStartDateTime: scheduleInfo.startDateTime,
    defaultEndDateTime: scheduleInfo.endDateTime,
    defaultMemo: scheduleInfo.memo,
  });

  const handleSubmitScheduleModifyForm = (e: React.FormEvent) => {
    e.preventDefault();

    const body = {
      title: validationSchedule.title.inputValue,
      startDateTime: validationSchedule.startDateTime.inputValue,
      endDateTime: validationSchedule.endDateTime.inputValue,
      memo: validationSchedule.memo.inputValue,
    };

    mutate(body);
  };

  const handleClickAllDayButton = () => {
    setAllDay((prev) => !prev);
  };

  const getDateFieldsetProps = (dateTime: string) =>
    isAllDay
      ? {
          type: 'date',
          defaultValue: getISODateString(dateTime),
        }
      : {
          type: 'datetime-local',
          defaultValue: dateTime,
        };

  const startDateFieldsetProps = getDateFieldsetProps(scheduleInfo.startDateTime);
  const endDateFieldsetProps = getDateFieldsetProps(scheduleInfo.endDateTime);

  return (
    <div css={modalStyle}>
      <form css={formStyle} onSubmit={handleSubmitScheduleModifyForm}>
        <div css={categoryStyle(theme, scheduleInfo.colorCode)}>{data?.data.name}</div>
        <Fieldset
          placeholder="제목을 입력하세요."
          defaultValue={scheduleInfo.title}
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
        />
        <Button cssProp={allDayButtonStyle(theme, isAllDay)} onClick={handleClickAllDayButton}>
          종일
        </Button>
        <div css={dateTimeStyle} key={startDateFieldsetProps.type}>
          <Fieldset
            type={startDateFieldsetProps.type}
            defaultValue={startDateFieldsetProps.defaultValue}
            onChange={validationSchedule.startDateTime.onChangeValue}
          />
          <p css={arrowStyle}>↓</p>
          <Fieldset
            type={endDateFieldsetProps.type}
            defaultValue={endDateFieldsetProps.defaultValue}
            onChange={validationSchedule.endDateTime.onChangeValue}
          />
        </div>
        <Fieldset
          placeholder="메모를 추가하세요."
          defaultValue={scheduleInfo.memo}
          onChange={validationSchedule.memo.onChangeValue}
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
