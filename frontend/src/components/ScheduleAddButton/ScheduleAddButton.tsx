import { useTheme } from '@emotion/react';

import Button from '@/components/@common/Button/Button';

import { MdEditCalendar } from 'react-icons/md';

import { scheduleAddButton } from './ScheduleAddButton.styles';

interface ScheduleAddButtonProps {
  onClick: () => void;
}

function ScheduleAddButton({ onClick }: ScheduleAddButtonProps) {
  const theme = useTheme();

  return (
    <Button cssProp={scheduleAddButton(theme)} onClick={onClick}>
      <MdEditCalendar />
    </Button>
  );
}

export default ScheduleAddButton;
