import { ComponentMeta, ComponentStory } from '@storybook/react';

import SideBarFallback from './SideBar.fallback';

export default {
  title: 'Components/SideBarFallback',
  component: SideBarFallback,
} as ComponentMeta<typeof SideBarFallback>;

const Template: ComponentStory<typeof SideBarFallback> = () => <SideBarFallback />;

const Primary = Template.bind({});

export { Primary };
