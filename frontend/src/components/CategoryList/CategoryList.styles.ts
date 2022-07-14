import { css, Theme } from '@emotion/react';

const categoryLayout = ({ colors, flex }: Theme) => css`
  ${flex.row}

  justify-content:space-around;

  width: 90%;
  height: 12rem;
  margin: auto;
  border-bottom: 2px solid ${colors.GRAY_400};

  background: ${colors.GRAY_100};

  font-weight: 700;
`;

export { categoryLayout };
