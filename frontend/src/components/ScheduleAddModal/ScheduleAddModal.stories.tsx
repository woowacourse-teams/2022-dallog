import { ComponentStory, ComponentMeta } from '@storybook/react';

import ScheduleAddModal from './ScheduleAddModal';

export default {
  title: 'Components/ScheduleAddModal',
  component: ScheduleAddModal,
} as ComponentMeta<typeof ScheduleAddModal>;

const Template: ComponentStory<typeof ScheduleAddModal> = () => <ScheduleAddModal />;

export const Primary = Template.bind({});
