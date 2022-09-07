import { ComponentMeta, ComponentStory } from '@storybook/react';

import WithdrawalModal from './WithdrawalModal';

export default {
  title: 'Components/WithdrawalModal',
  component: WithdrawalModal,
} as ComponentMeta<typeof WithdrawalModal>;

const Template: ComponentStory<typeof WithdrawalModal> = (args) => <WithdrawalModal {...args} />;

export const Primary = Template.bind({});
