import { css, Theme } from '@emotion/react';

const buttonStyle = ({ colors }: Theme) => css`
  width: 8rem;
  height: 8rem;

  background: transparent;

  color: ${colors.GRAY_700};

  &:hover {
    border-radius: 50%;

    background: ${colors.GRAY_100};

    filter: none;
  }
`;

const categoryItemStyle = ({ colors, flex }: Theme) => css`
  ${flex.row}

  justify-content: space-around;

  height: 20rem;
  border-bottom: 1px solid ${colors.GRAY_400};

  font-size: 4rem;
`;

const itemStyle = css`
  flex: 1 1 0;
  text-align: center;
`;

export { buttonStyle, categoryItemStyle, itemStyle };
