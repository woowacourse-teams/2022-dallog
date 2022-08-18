import { css } from '@emotion/react';
import { ComponentMeta, ComponentStory } from '@storybook/react';

import Skeleton from './Skeleton';

export default {
  title: 'Components/@Common/Skeleton',
  component: Skeleton,
} as ComponentMeta<typeof Skeleton>;

const Template: ComponentStory<typeof Skeleton> = (args) => <Skeleton {...args} />;

const Primary = Template.bind({});
Primary.args = {
  cssProp: css`
    width: 100rem;
    height: 20rem;
  `,
};
export { Primary };
