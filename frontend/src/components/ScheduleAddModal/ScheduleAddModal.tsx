import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useRef, useState } from 'react';
import { useMutation, useQueryClient } from 'react-query';

import { ScheduleType } from '@/@types/schedule';

import Button from '@/components/@common/Button/Button';
import FieldSet from '@/components/@common/FieldSet/FieldSet';

import { CACHE_KEY } from '@/constants';

import { createPostBody } from '@/utils';
import { getDate, getDateTime } from '@/utils/date';

import scheduleApi from '@/api/schedule';

import {
  allDayButton,
  arrow,
  cancelButton,
  controlButtons,
  dateTime,
  form,
  saveButton,
  scheduleAddModal,
} from './ScheduleAddModal.styles';

interface ScheduleAddModalProps {
  closeModal: () => void;
}

function ScheduleAddModal({ closeModal }: ScheduleAddModalProps) {
  const [isAllDay, setAllDay] = useState(true);
  const theme = useTheme();

  const queryClient = useQueryClient();
  const {
    isLoading,
    error,
    mutate: postSchedule,
  } = useMutation<
    AxiosResponse<{ schedules: ScheduleType[] }>,
    AxiosError,
    Omit<ScheduleType, 'id'>,
    unknown
  >(scheduleApi.post, {
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

  const handleClickScheduleAddModal = (e: React.MouseEvent) => {
    e.stopPropagation();
  };

  const handleSubmitScheduleAddForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const body = createPostBody(inputRef);

    if (!body) {
      return;
    }

    postSchedule(body);
  };

  const onSuccessPostSchedule = () => {
    queryClient.invalidateQueries(CACHE_KEY.SCHEDULES);
    closeModal();
  };

  if (isLoading) return <>Loading</>;

  if (error) return <>Error</>;

  const dateFieldSet = isAllDay
    ? {
        type: 'date',
        defaultValue: getDate(),
      }
    : {
        type: 'datetime-local',
        defaultValue: getDateTime(),
      };

  return (
    <div css={scheduleAddModal} onClick={handleClickScheduleAddModal}>
      <form css={form} onSubmit={handleSubmitScheduleAddForm}>
        <FieldSet placeholder="제목을 입력하세요." refProp={inputRef.title} />
        <Button cssProp={allDayButton(theme, isAllDay)} onClick={handleClickAllDayButton}>
          종일
        </Button>
        <div css={dateTime} key={dateFieldSet.type}>
          <FieldSet
            type={dateFieldSet.type}
            defaultValue={dateFieldSet.defaultValue}
            refProp={inputRef.startDateTime}
          />
          <p css={arrow}>↓</p>
          <FieldSet
            type={dateFieldSet.type}
            defaultValue={dateFieldSet.defaultValue}
            refProp={inputRef.endDateTime}
          />
        </div>
        <FieldSet placeholder="메모를 추가하세요." refProp={inputRef.memo} />
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
