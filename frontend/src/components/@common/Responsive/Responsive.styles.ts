import { css, Theme } from '@emotion/react';

const layoutStyle = ({ mq }: Theme, type: string) => css`
  display: none;

  width: 100%;
  height: 100%;

  ${type === 'laptop' && mq?.laptop} {
    display: block;
  }

  ${type === 'tablet' && mq?.tablet} {
    display: block;
  }

  ${type === 'mobile' && mq?.mobile} {
    display: block;
  }

  ${type === 'all'} {
    display: block;
  }
`;

export { layoutStyle };
