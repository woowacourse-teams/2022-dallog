import { ComponentMeta, ComponentStory } from '@storybook/react';

import CategoryItem from './CategoryItem';

export default {
  title: 'Components/CategoryItem',
  component: CategoryItem,
} as ComponentMeta<typeof CategoryItem>;

const Template: ComponentStory<typeof CategoryItem> = (args) => <CategoryItem {...args} />;

export const Primary = Template.bind({});
Primary.args = {
  category: { id: 1, name: '달록 공식 일정', createdAt: '2022-07-04T13:00:00' },
};
