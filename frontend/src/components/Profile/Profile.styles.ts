import { css, Theme } from '@emotion/react';

const contentStyle = ({ flex }: Theme) => css`
  ${flex.column};

  gap: 3rem;

  width: 100%;

  text-align: center;
`;

const emailStyle = ({ colors }: Theme) => css`
  font-size: 3rem;
  color: ${colors.GRAY_500};
`;

const imageStyle = css`
  width: 35rem;
  height: 35rem;
  border-radius: 50%;
`;

const inputStyle = {
  input: css`
    height: 3rem;

    font-size: 3rem;
  `,
};

const layoutStyle = ({ flex, colors }: Theme) => css`
  ${flex.column};

  justify-content: space-around;
  gap: 5rem;
  position: absolute;
  top: 15rem;
  right: 2rem;

  width: 60rem;
  padding: 5rem;
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

const withdrawalButtonStyle = ({ colors }: Theme) => css`
  padding: 2rem 3rem;
  border: 1px solid ${colors.RED_400};
  border-radius: 3px;

  font-size: 3rem;
  color: ${colors.RED_400};
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

  font-size: 3rem;
`;

const nameStyle = css`
  margin-left: 7rem;

  font-size: 3.5rem;
`;

const skeletonStyle = ({ flex }: Theme) => css`
  ${flex.column};

  gap: 3rem;
`;

export {
  contentStyle,
  emailStyle,
  imageStyle,
  inputStyle,
  layoutStyle,
  logoutButtonStyle,
  menu,
  menuTitle,
  nameStyle,
  nameButtonStyle,
  skeletonStyle,
  withdrawalButtonStyle,
};
