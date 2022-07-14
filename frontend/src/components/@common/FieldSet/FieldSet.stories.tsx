import { ComponentMeta, ComponentStory } from '@storybook/react';

import FieldSet from './FieldSet';

export default {
  title: 'Components/@Common/FieldSet',
  component: FieldSet,
} as ComponentMeta<typeof FieldSet>;

const Template: ComponentStory<typeof FieldSet> = (args) => <FieldSet {...args} />;

const Primary = Template.bind({});
Primary.args = {
  id: 'primary',
  labelText: 'primary',
  placeholder: '입력해주세요.',
};

const DatePicker = Template.bind({});
DatePicker.args = {
  type: 'datetime-local',
  id: 'date-time-picker',
  labelText: '일정 시작',
  placeholder: '입력해주세요.',
};

export { Primary, DatePicker };
