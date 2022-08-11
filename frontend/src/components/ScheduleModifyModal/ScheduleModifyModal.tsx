import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useState } from 'react';
import { useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import { CategoryType } from '@/@types/category';
import { ScheduleType } from '@/@types/schedule';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';

import { CACHE_KEY } from '@/constants';

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
        <Fieldset placeholder="제목을 입력하세요." defaultValue={scheduleInfo.title} />
        <Button cssProp={allDayButton(theme, isAllDay)} onClick={handleClickAllDayButton}>
          종일
        </Button>
        <div css={dateTime} key={startDateFieldsetProps.type}>
          <Fieldset
            type={startDateFieldsetProps.type}
            defaultValue={startDateFieldsetProps.defaultValue}
          />
          <p css={arrow}>↓</p>
          <Fieldset
            type={endDateFieldsetProps.type}
            defaultValue={endDateFieldsetProps.defaultValue}
          />
        </div>
        <Fieldset placeholder="메모를 추가하세요." defaultValue={scheduleInfo.memo} />
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

export default ScheduleModifyModal;
