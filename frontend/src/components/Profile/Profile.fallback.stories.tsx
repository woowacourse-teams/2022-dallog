import { css } from '@emotion/react';
import { ComponentMeta, ComponentStory } from '@storybook/react';

import ProfileFallback from './Profile.fallback';

export default {
  title: 'Components/ProfileFallback',
  component: ProfileFallback,
} as ComponentMeta<typeof ProfileFallback>;

const Template: ComponentStory<typeof ProfileFallback> = () => <ProfileFallback />;

const Primary = Template.bind({});
Primary.args = {
  cssProp: css`
    width: 100rem;
    height: 20rem;
  `,
};
export { Primary };
