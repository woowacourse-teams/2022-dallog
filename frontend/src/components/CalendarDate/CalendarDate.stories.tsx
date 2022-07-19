import { ComponentMeta, ComponentStory } from '@storybook/react';

import CalendarDate from './CalendarDate';

export default {
  title: 'Components/CalendarDate',
  component: CalendarDate,
} as ComponentMeta<typeof CalendarDate>;

const Template: ComponentStory<typeof CalendarDate> = (args) => <CalendarDate {...args} />;

export const Primary = Template.bind({});
Primary.args = {
  dateInfo: {
    year: 2022,
    month: 7,
    date: 22,
    day: 0,
  },
};
