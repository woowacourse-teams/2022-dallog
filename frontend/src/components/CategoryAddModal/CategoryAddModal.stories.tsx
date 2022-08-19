import { ComponentMeta, ComponentStory } from '@storybook/react';

import CategoryAddModal from './CategoryAddModal';

export default {
  title: 'Components/CategoryAddModal',
  component: CategoryAddModal,
} as ComponentMeta<typeof CategoryAddModal>;

const Template: ComponentStory<typeof CategoryAddModal> = (args) => <CategoryAddModal {...args} />;

export const Primary = Template.bind({});
