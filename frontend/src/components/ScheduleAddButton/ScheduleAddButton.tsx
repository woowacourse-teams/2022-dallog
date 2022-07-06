import { useTheme } from '@emotion/react';

import { scheduleAddButton } from './ScheduleAddButton.styles';

import Button from '../@common/Button/Button';

function ScheduleAddButton() {
  const theme = useTheme();

  return <Button cssProp={scheduleAddButton(theme)}>+</Button>;
}

export default ScheduleAddButton;
