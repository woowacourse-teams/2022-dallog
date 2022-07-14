import { ComponentStory, ComponentMeta } from '@storybook/react';

import CategoryList from './CategoryList';

import { categoryDB } from '@/mocks/data';

export default {
  title: 'Components/CategoryList',
  component: CategoryList,
} as ComponentMeta<typeof CategoryList>;

const Template: ComponentStory<typeof CategoryList> = (args) => <CategoryList {...args} />;

export const Primary = Template.bind({});
Primary.args = {
  categoryList: categoryDB.data,
};
