import { ComponentMeta, ComponentStory } from '@storybook/react';

import ScheduleAddModal from './ScheduleAddModal';

export default {
  title: 'Components/ScheduleAddModal',
  component: ScheduleAddModal,
} as ComponentMeta<typeof ScheduleAddModal>;

const Template: ComponentStory<typeof ScheduleAddModal> = (args) => <ScheduleAddModal {...args} />;

export const Primary = Template.bind({});
Primary.args = {
  dateInfo: '2022-08-02T00:00',
};
