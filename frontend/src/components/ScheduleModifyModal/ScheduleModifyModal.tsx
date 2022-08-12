import { validateLength } from '@/validation';
import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useState } from 'react';
import { useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import useValidateSchedule from '@/hooks/useValidateSchedule';

import { CategoryType } from '@/@types/category';
import { ScheduleType } from '@/@types/schedule';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';

import { CACHE_KEY, VALIDATION_MESSAGE } from '@/constants';

import { checkAllDay, getISODateString } from '@/utils/date';

import categoryApi from '@/api/category';

import {
  allDayButton,
  arrow,
  cancelButton,
  categorySelect,
  controlButtons,
  dateTime,
  form,
  saveButton,
  scheduleModifyModal,
} from './ScheduleModifyModal.styles';

interface ScheduleModifyModalProps {
  scheduleInfo: ScheduleType | null;
  closeModal: () => void;
}

function ScheduleModifyModal({ scheduleInfo, closeModal }: ScheduleModifyModalProps) {
  const { accessToken } = useRecoilValue(userState);
  const theme = useTheme();

  const [isAllDay, setAllDay] = useState(
    !!checkAllDay(scheduleInfo?.startDateTime, scheduleInfo?.endDateTime)
  );

  const { data } = useQuery<AxiosResponse<CategoryType[]>, AxiosError>(
    CACHE_KEY.MY_CATEGORIES,
    () => categoryApi.getMy(accessToken)
  );

  const validationSchedule = useValidateSchedule({
    defaultTitle: scheduleInfo?.title,
    defaultStartDateTime: scheduleInfo?.startDateTime,
    defaultEndDateTime: scheduleInfo?.endDateTime,
    defaultMemo: scheduleInfo?.memo,
  });

  if (scheduleInfo === null) {
    return <>Error</>;
  }

  const handleSubmitScheduleModifyForm = (e: React.FormEvent) => {
    e.preventDefault();
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
    <div css={scheduleModifyModal}>
      <form css={form} onSubmit={handleSubmitScheduleModifyForm}>
        <select id="myCategories" defaultValue={scheduleInfo.categoryId} css={categorySelect}>
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
          defaultValue={scheduleInfo.title}
          onChange={validationSchedule.title.onChange}
          isValid={validateLength(validationSchedule.title.inputValue, 1, 50)}
          errorMessage={VALIDATION_MESSAGE.STRING_LENGTH(1, 50)}
        />
        <Button cssProp={allDayButton(theme, isAllDay)} onClick={handleClickAllDayButton}>
          종일
        </Button>
        <div css={dateTime} key={startDateFieldsetProps.type}>
          <Fieldset
            type={startDateFieldsetProps.type}
            defaultValue={startDateFieldsetProps.defaultValue}
            onChange={validationSchedule.startDateTime.onChange}
          />
          <p css={arrow}>↓</p>
          <Fieldset
            type={endDateFieldsetProps.type}
            defaultValue={endDateFieldsetProps.defaultValue}
            onChange={validationSchedule.endDateTime.onChange}
          />
        </div>
        <Fieldset
          placeholder="메모를 추가하세요."
          defaultValue={scheduleInfo.memo}
          onChange={validationSchedule.memo.onChange}
          isValid={validateLength(validationSchedule.memo.inputValue, 1, 255)}
          errorMessage={VALIDATION_MESSAGE.STRING_LENGTH(1, 255)}
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

export default ScheduleModifyModal;
