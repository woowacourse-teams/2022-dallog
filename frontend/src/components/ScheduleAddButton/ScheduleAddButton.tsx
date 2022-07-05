/* eslint-disable import/named */
import { useTheme } from '@emotion/react';

import { scheduleAddButton } from './ScheduleAddButton.styles';

import Button from '../@common/Button';

function ScheduleAddButton() {
  const theme = useTheme();

  return <Button css={scheduleAddButton(theme)}>+</Button>;
}

export default ScheduleAddButton;
