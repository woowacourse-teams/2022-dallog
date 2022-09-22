import { ComponentMeta, ComponentStory } from '@storybook/react';

import { TIMES } from '@/constants/date';

import Select from './Select';

export default {
  title: 'Components/@Common/Select',
  component: Select,
} as ComponentMeta<typeof Select>;

const Template: ComponentStory<typeof Select> = (args) => <Select {...args} />;

const Primary = Template.bind({});
Primary.args = {
  options: TIMES,
};

export { Primary };
