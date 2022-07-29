import { css, Theme } from '@emotion/react';

const itemStyle = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 100%;
`;

const nameStyle = css`
  overflow: hidden;
  position: relative;

  width: 32rem;

  white-space: nowrap;
  text-overflow: ellipsis;

  &:hover {
    cursor: pointer;
  }
`;

const controlButtonsStyle = ({ flex }: Theme) => css`
  ${flex.row}
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

export { buttonStyle, controlButtonsStyle, itemStyle, nameStyle };
