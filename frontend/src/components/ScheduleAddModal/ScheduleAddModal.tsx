import { useRef } from 'react';

import { RefetchOptions, RefetchQueryFilters, useMutation, QueryObserverResult } from 'react-query';
import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';

import { Schedule } from '@/@types';

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

import Button from '../@common/Button/Button';
import FieldSet from '../@common/FieldSet/FieldSet';

import { createPostBody } from '@/utils';

import scheduleApi from '@/api/schedule';

interface ScheduleAddModalProps {
  closeModal: () => void;
  refetch: <TPageData>(
    options?: (RefetchOptions & RefetchQueryFilters<TPageData>) | undefined
  ) => Promise<QueryObserverResult<AxiosResponse<{ schedules: Schedule[] }>, AxiosError>>;
}

function ScheduleAddModal({ closeModal, refetch }: ScheduleAddModalProps) {
  const theme = useTheme();

  const {
    isLoading,
    error,
    mutate: postSchedule,
  } = useMutation<
    AxiosResponse<{ schedules: Schedule[] }>,
    AxiosError,
    Omit<Schedule, 'id'>,
    unknown
  >(scheduleApi.post, {
    onSuccess: () => {
      onSuccessPostSchedule();
    },
  });

  const inputRef = {
    title: useRef<HTMLInputElement>(null),
    startDateTime: useRef<HTMLInputElement>(null),
    endDateTime: useRef<HTMLInputElement>(null),
    memo: useRef<HTMLInputElement>(null),
  };

  const handleClickScheduleAddModal = (e: React.MouseEvent) => {
    e.stopPropagation();
  };

  const handleSubmitScheduleAddForm = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const body = createPostBody(inputRef);

    if (!body) {
      return;
    }

    postSchedule(body);
  };

  const onSuccessPostSchedule = () => {
    refetch();
    closeModal();
  };

  if (isLoading) return <>Loading</>;

  if (error) return <>Error</>;

  return (
    <div css={scheduleAddModal} onClick={handleClickScheduleAddModal}>
      <form css={form} onSubmit={handleSubmitScheduleAddForm}>
        <FieldSet placeholder="제목을 입력하세요." refProp={inputRef.title} />
        <Button cssProp={allDayButton(theme)}>종일</Button>
        <div css={dateTime(theme)}>
          <FieldSet type="datetime-local" refProp={inputRef.startDateTime} />
          <p css={arrow(theme)}>↓</p>
          <FieldSet type="datetime-local" refProp={inputRef.endDateTime} />
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
