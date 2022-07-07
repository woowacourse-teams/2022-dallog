import { useRef } from 'react';

import { useTheme } from '@emotion/react';

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

interface ScheduleAddModalProps {
  closeModal: () => void;
}

function ScheduleAddModal({ closeModal }: ScheduleAddModalProps) {
  const theme = useTheme();

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

    const isValidRef = Object.values(inputRef).every(
      (inputRef) => inputRef.current instanceof HTMLInputElement
    );

    if (!isValidRef) {
      return;
    }

    const [title, startDateTime, endDateTime, memo] = Object.values(inputRef);

    const body = await JSON.stringify({
      title: title.current?.value,
      startDateTime: startDateTime.current?.value,
      endDateTime: endDateTime.current?.value,
      memo: memo.current?.value,
    });

    await fetch('/api/schedules', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json',
      },
      body,
    });
  };

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
