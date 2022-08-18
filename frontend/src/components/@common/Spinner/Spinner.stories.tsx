import { ComponentMeta, ComponentStory } from '@storybook/react';

import Spinner from './Spinner';

export default {
  title: 'Components/@Common/Spinner',
  component: Spinner,
} as ComponentMeta<typeof Spinner>;

const Template: ComponentStory<typeof Spinner> = () => <Spinner />;

const Primary = Template.bind({});
Primary.args = {};
