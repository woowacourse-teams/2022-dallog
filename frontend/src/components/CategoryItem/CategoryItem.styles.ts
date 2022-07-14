import { css, Theme } from '@emotion/react';

const categoryLayout = ({ colors, flex }: Theme) => css`
  ${flex.row}
  justify-content:space-around;

  width: 90%;
  height: 20rem;
  margin: auto;
  border-bottom: 1px solid ${colors.GRAY_400};
`;

export { categoryLayout };
