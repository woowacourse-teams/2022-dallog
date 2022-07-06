import { ComponentStory, ComponentMeta } from '@storybook/react';

import ScheduleAddButton from './ScheduleAddButton';

export default {
  title: 'Components/ScheduleAddButton',
  component: ScheduleAddButton,
} as ComponentMeta<typeof ScheduleAddButton>;

const Template: ComponentStory<typeof ScheduleAddButton> = () => <ScheduleAddButton />;

export const Primary = Template.bind({});
