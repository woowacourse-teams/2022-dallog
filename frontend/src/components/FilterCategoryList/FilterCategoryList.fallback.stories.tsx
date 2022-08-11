import { ComponentMeta, ComponentStory } from '@storybook/react';

import FilterCategoryListFallback from './FilterCategoryList.fallback';

export default {
  title: 'Components/FilterCategoryListFallback',
  component: FilterCategoryListFallback,
} as ComponentMeta<typeof FilterCategoryListFallback>;

const Template: ComponentStory<typeof FilterCategoryListFallback> = () => (
  <FilterCategoryListFallback />
);

const Primary = Template.bind({});

export { Primary };
