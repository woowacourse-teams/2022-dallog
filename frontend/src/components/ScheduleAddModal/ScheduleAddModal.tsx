import { useRef } from 'react';

import { useMutation } from 'react-query';
import { useTheme } from '@emotion/react';

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

import scheduleApi from '@/api/schedule';

interface ScheduleAddModalProps {
  closeModal: () => void;
  setSchedule: React.Dispatch<React.SetStateAction<Schedule[]>>;
}

function ScheduleAddModal({ closeModal, setSchedule }: ScheduleAddModalProps) {
  const theme = useTheme();

  const { isLoading, error, mutate: postSchedule } = useMutation(scheduleApi.post);

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

    const [title, startDateTime, endDateTime, memo] = Object.values(inputRef).map(
      (el) => el.current
    );

    if (
      !(title instanceof HTMLInputElement) ||
      !(memo instanceof HTMLInputElement) ||
      !(startDateTime instanceof HTMLInputElement) ||
      !(endDateTime instanceof HTMLInputElement)
    ) {
      return;
    }

    const body = {
      title: title.value,
      startDateTime: startDateTime.value,
      endDateTime: endDateTime.value,
      memo: memo.value,
    };

    postSchedule(body);
    setSchedule((prev) => [...prev, { id: prev.length, ...body }]);
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
