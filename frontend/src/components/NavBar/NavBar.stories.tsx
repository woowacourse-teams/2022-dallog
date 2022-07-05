// eslint-disable-next-line import/named
import { ComponentStory, ComponentMeta } from '@storybook/react';

import NavBar from './NavBar';

export default {
  title: 'Components/NavBar',
  component: NavBar,
} as ComponentMeta<typeof NavBar>;

const Template: ComponentStory<typeof NavBar> = () => <NavBar />;

export const Primary = Template.bind({});
