import { css, Theme } from '@emotion/react';

const hrStyle = ({ colors }: Theme) => css`
  width: 85%;
  height: 1rem;

  border-top: 1px solid ${colors.GRAY_100};
`;

const imageStyle = css`
  width: 38.5rem;
  height: 38.5rem;
  border-radius: 50%;
`;

const inputStyle = {
  div: css`
    width: 30rem;
    height: 3.5rem;

    font-size: 3.5rem;
  `,
  label: css`
    height: 0%;
  `,
  input: css`
    height: 3.5rem;

    font-size: 3.5rem;
  `,
};

const layoutStyle = ({ flex, colors }: Theme) => css`
  ${flex.column};

  justify-content: space-around;

  width: 80rem;
  height: 100rem;
  box-shadow: 0 0 1px 1px rgba(0, 0, 0, 0.25);
  border-radius: 10px;

  background: ${colors.WHITE};

  font-size: 4rem;
`;

const logoutButtonStyle = ({ colors }: Theme) => css`
  padding: 2rem 3rem;
  border: 1px solid ${colors.GRAY_400};
  border-radius: 3px;

  font-size: 3rem;
`;

const menu = ({ colors }: Theme) => css`
  position: relative;

  width: 9rem;
  height: 9rem;

  &:hover {
    border-radius: 50%;

    background: ${colors.GRAY_100};

    filter: none;
  }

  &:hover span {
    visibility: visible;
  }
`;

const menuTitle = ({ colors }: Theme) => css`
  visibility: hidden;
  position: absolute;
  top: 120%;
  left: 50%;
  transform: translateX(-50%);

  padding: 2rem 3rem;

  background: ${colors.GRAY_700}ee;

  font-size: 3rem;
  font-weight: normal;
  color: ${colors.WHITE};
  white-space: nowrap;
`;

const nameButtonStyle = ({ flex }: Theme) => css`
  ${flex.row};

  justify-content: flex-end;

  gap: 2rem;
`;

const spinnerStyle = ({ flex }: Theme) => css`
  ${flex.row}

  width: 100%;
  height: 100%;
`;

export {
  hrStyle,
  imageStyle,
  inputStyle,
  layoutStyle,
  logoutButtonStyle,
  menu,
  menuTitle,
  nameButtonStyle,
  spinnerStyle,
};
