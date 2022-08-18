import { css, Theme } from '@emotion/react';

const itemStyle = css`
  flex: 1 1 0;
  text-align: center;
`;

const headerStyle = ({ flex, colors }: Theme) => css`
  ${flex.row}

  justify-content: space-around;

  width: 100%;
  height: 12rem;
  border-bottom: 2px solid ${colors.GRAY_400};

  background: ${colors.GRAY_100};

  font-size: 4rem;
  font-weight: 700;
`;

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

const listStyle = css`
  width: 100%;
  height: 100%;

  overflow-y: overlay;
`;

export { buttonStyle, listStyle, headerStyle, itemStyle };
