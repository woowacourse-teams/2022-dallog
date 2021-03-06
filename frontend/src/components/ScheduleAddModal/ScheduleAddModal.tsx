import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useRef, useState } from 'react';
import { QueryObserverResult, RefetchOptions, RefetchQueryFilters, useMutation } from 'react-query';

import { Schedule } from '@/@types';

import { createPostBody } from '@/utils';
import { getDate, getDateTime } from '@/utils/date';

import scheduleApi from '@/api/schedule';

import Button from '../@common/Button/Button';
import FieldSet from '../@common/FieldSet/FieldSet';
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
  refetch: <TPageData>(
    options?: (RefetchOptions & RefetchQueryFilters<TPageData>) | undefined
  ) => Promise<QueryObserverResult<AxiosResponse<{ schedules: Schedule[] }>, AxiosError>>;
}

function ScheduleAddModal({ closeModal, refetch }: ScheduleAddModalProps) {
  const [isAllDay, setAllDay] = useState(true);
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
    refetch();
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
        <FieldSet placeholder="????????? ???????????????." refProp={inputRef.title} />
        <Button cssProp={allDayButton(theme, isAllDay)} onClick={handleClickAllDayButton}>
          ??????
        </Button>
        <div css={dateTime} key={dateFieldSet.type}>
          <FieldSet
            type={dateFieldSet.type}
            defaultValue={dateFieldSet.defaultValue}
            refProp={inputRef.startDateTime}
          />
          <p css={arrow}>???</p>
          <FieldSet
            type={dateFieldSet.type}
            defaultValue={dateFieldSet.defaultValue}
            refProp={inputRef.endDateTime}
          />
        </div>
        <FieldSet placeholder="????????? ???????????????." refProp={inputRef.memo} />
        <div css={controlButtons}>
          <Button cssProp={cancelButton(theme)} onClick={closeModal}>
            ??????
          </Button>
          <Button type="submit" cssProp={saveButton(theme)}>
            ??????
          </Button>
        </div>
      </form>
    </div>
  );
}

export default ScheduleAddModal;
