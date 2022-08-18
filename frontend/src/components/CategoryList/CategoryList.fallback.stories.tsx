import { ComponentMeta, ComponentStory } from '@storybook/react';

import CategoryListFallback from './CategoryList.fallback';

export default {
  title: 'Components/CategoryListFallback',
  component: CategoryListFallback,
} as ComponentMeta<typeof CategoryListFallback>;

const Template: ComponentStory<typeof CategoryListFallback> = () => <CategoryListFallback />;

const Primary = Template.bind({});

export { Primary };
