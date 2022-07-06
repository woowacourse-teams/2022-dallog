import { ComponentStory, ComponentMeta } from '@storybook/react';

import ScheduleAddButton from './ScheduleAddButton';

export default {
  title: 'Components/ScheduleAddButton',
  component: ScheduleAddButton,
} as ComponentMeta<typeof ScheduleAddButton>;

const Template: ComponentStory<typeof ScheduleAddButton> = (args) => (
  <ScheduleAddButton {...args} />
);

export const Primary = Template.bind({});
Primary.args = {
  onClick: () => void 0,
};
