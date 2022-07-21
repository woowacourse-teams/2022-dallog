import { ComponentMeta, ComponentStory } from '@storybook/react';

import SideBar from './SideBar';

export default {
  title: 'Components/SideBar',
  component: SideBar,
} as ComponentMeta<typeof SideBar>;

const Template: ComponentStory<typeof SideBar> = (args) => <SideBar {...args} />;

export const Primary = Template.bind({});
