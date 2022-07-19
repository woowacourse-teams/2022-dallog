import { css, Theme } from '@emotion/react';

const loginModal = ({ colors, flex }: Theme) => css`
  ${flex.column}

  width: 120rem;
  height: 160rem;
  padding: 12.5rem;
  border-radius: 12px;

  background: ${colors.WHITE};
`;

const title = ({ colors }: Theme) => css`
  font-size: 8rem;
  font-weight: bold;
  color: ${colors.GRAY_700};
`;

const content = ({ flex }: Theme) => css`
  ${flex.column}

  height: 100%;
`;

const loginButton = ({ colors, flex }: Theme) => css`
  ${flex.row}

  position: relative;
  justify-content: flex-start;

  width: 75rem;
  height: 15rem;
  padding: 4rem;
  border: 1px solid ${colors.GRAY_400};
  border-radius: 8px;
  box-shadow: 2px 2px 2px ${colors.GRAY_400};

  background: ${colors.WHITE};

  font-size: 4rem;
  color: ${colors.BLACK}8a;

  &:hover {
    box-shadow: 3px 3px 3px ${colors.GRAY_500};
  }
`;

const loginText = css`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);

  width: 100%;
`;

export { content, loginButton, loginModal, loginText, title };
