import { ComponentMeta, ComponentStory } from '@storybook/react';

import { categoryDB } from '@/mocks/data';

import CategoryList from './CategoryList';

export default {
  title: 'Components/CategoryList',
  component: CategoryList,
} as ComponentMeta<typeof CategoryList>;

const Template: ComponentStory<typeof CategoryList> = (args) => <CategoryList {...args} />;

export const Primary = Template.bind({});
Primary.args = {
  categoryList: categoryDB.data,
};
