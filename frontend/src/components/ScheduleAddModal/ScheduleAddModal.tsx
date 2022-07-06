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

  const handleClickScheduleAddModal = (e: React.MouseEvent) => {
    e.stopPropagation();
  };

  return (
    <div css={scheduleAddModal} onClick={handleClickScheduleAddModal}>
      <form css={form}>
        <FieldSet placeholder="제목을 입력하세요." />
        <Button cssProp={allDayButton(theme)}>종일</Button>
        <div css={dateTime(theme)}>
          <FieldSet type="datetime-local" />
          <p css={arrow(theme)}>↓</p>
          <FieldSet type="datetime-local" />
        </div>
        <FieldSet placeholder="메모를 추가하세요." />
        <div css={controlButtons}>
          <Button cssProp={cancelButton(theme)} onClick={closeModal}>
            취소
          </Button>
          <Button type="submit" cssProp={saveButton(theme)} onClick={closeModal}>
            저장
          </Button>
        </div>
      </form>
    </div>
  );
}

export default ScheduleAddModal;
