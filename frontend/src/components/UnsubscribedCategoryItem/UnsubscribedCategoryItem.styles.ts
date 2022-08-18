import { css, Theme } from '@emotion/react';

const categoryItem = ({ colors, flex }: Theme) => css`
  ${flex.row}

  justify-content:space-around;

  height: 20rem;
  border-bottom: 1px solid ${colors.GRAY_400};

  font-size: 4rem;
`;

const item = css`
  flex: 1 1 0;
  text-align: center;
`;

const subscribeButton = ({ colors }: Theme) => css`
  width: 15rem;
  height: 8rem;
  border-radius: 3px;

  background-color: ${colors.YELLOW_500};

  font-size: 3.5rem;
  font-weight: 700;
  line-height: 3.5rem;
  color: ${colors.WHITE};
`;

export { categoryItem, item, subscribeButton };
