import { useTheme } from '@emotion/react';

import Button from '@/components/@common/Button/Button';

import { BsCalendarPlusFill } from 'react-icons/bs';

import { scheduleAddButton } from './ScheduleAddButton.styles';

interface ScheduleAddButtonProps {
  onClick: () => void;
}

function ScheduleAddButton({ onClick }: ScheduleAddButtonProps) {
  const theme = useTheme();

  return (
    <Button cssProp={scheduleAddButton(theme)} onClick={onClick}>
      <BsCalendarPlusFill />
    </Button>
  );
}

export default ScheduleAddButton;
