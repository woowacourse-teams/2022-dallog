import { ComponentMeta, ComponentStory } from '@storybook/react';

import Fieldset from './Fieldset';

export default {
  title: 'Components/@Common/Fieldset',
  component: Fieldset,
} as ComponentMeta<typeof Fieldset>;

const Template: ComponentStory<typeof Fieldset> = (args) => <Fieldset {...args} />;

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
