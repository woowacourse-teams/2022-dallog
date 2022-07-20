import { css, Theme } from '@emotion/react';

const subscribeButton = ({ colors }: Theme, isSubscribe: boolean) => css`
  width: 15rem;
  height: 8rem;
  border-radius: 3px;

  background-color: ${isSubscribe ? colors.GRAY_500 : colors.YELLOW_500};

  font-size: 3.5rem;
  font-weight: 700;
  line-height: 3.5rem;
  color: ${colors.WHITE};
`;

export { subscribeButton };
