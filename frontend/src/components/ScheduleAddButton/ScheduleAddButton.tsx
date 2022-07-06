import { useTheme } from '@emotion/react';

import { scheduleAddButton } from './ScheduleAddButton.styles';

import Button from '../@common/Button/Button';

interface ScheduleAddButtonProps {
  onClick: () => void;
}

function ScheduleAddButton({ onClick }: ScheduleAddButtonProps) {
  const theme = useTheme();

  return (
    <Button cssProp={scheduleAddButton(theme)} onClick={onClick}>
      +
    </Button>
  );
}

export default ScheduleAddButton;
