import { ComponentMeta, ComponentStory } from '@storybook/react';

import SubscribeButton from './SubscribeButton';

export default {
  title: 'Components/SubscribeButton',
  component: SubscribeButton,
} as ComponentMeta<typeof SubscribeButton>;

const Template: ComponentStory<typeof SubscribeButton> = (args) => <SubscribeButton {...args} />;

export const Primary = Template.bind({});
Primary.args = {
  isSubscribing: true,
};
