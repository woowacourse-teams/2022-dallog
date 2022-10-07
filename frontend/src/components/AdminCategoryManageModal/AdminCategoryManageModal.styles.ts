import { css, Theme } from '@emotion/react';

const closeModalButtonStyle = css`
  position: absolute;
  top: 6rem;
  right: 6rem;

  font-size: 6rem;
`;

const deleteButtonStyle = ({ colors }: Theme) => css`
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 2px ${colors.GRAY_400};

  background: ${colors.RED_400};

  font-size: 3rem;
  color: ${colors.WHITE};
`;

const headerStyle = css`
  font-size: 6rem;
`;

const forgiveButtonStyle = ({ colors }: Theme) => css`
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 2px ${colors.GRAY_400};

  background: ${colors.RED_400};

  font-size: 3rem;
  color: ${colors.WHITE};
`;

const layoutStyle = ({ flex, colors }: Theme) => css`
  ${flex.column};

  justify-content: space-between;
  gap: 10rem;
  overflow: overlay;
  position: relative;

  max-height: 100vh;
  padding: 12.5rem;
  border-radius: 12px;

  background: ${colors.WHITE};
`;

const listBundleStyle = ({ flex }: Theme) => css`
  ${flex.row};

  justify-content: space-between;
  align-items: flex-start;
  gap: 4rem;

  width: 100%;
`;

const listSectionStyle = ({ flex }: Theme) => css`
  ${flex.column};

  gap: 4rem;
  justify-content: flex-start;
  align-items: flex-start;

  width: 60rem;
`;

const renameButtonStyle = ({ colors }: Theme) => css`
  height: 8rem;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 2px ${colors.GRAY_400};

  background: ${colors.YELLOW_500};

  font-size: 3rem;
  color: ${colors.WHITE};
`;

const renameFieldSetStyle = {
  div: css`
    width: 40%;
  `,
  input: css`
    height: 8rem;
  `,
};

const renameFormStyle = ({ flex }: Theme) => css`
  ${flex.row};

  justify-content: center;
  align-items: flex-start;
  gap: 4rem;
`;

const sectionStyle = css`
  width: 100%;
`;

const subscriberListStyle = ({ colors }: Theme) => css`
  overflow: hidden;

  width: 60rem;
  max-height: 50rem;
  padding-right: 2rem;
  border: 1px solid ${colors.GRAY_100};

  &:hover {
    overflow: overlay;
  }
`;

const spaceBetweenStyle = ({ flex }: Theme) => css`
  ${flex.row};

  align-items: flex-start;
  justify-content: space-between;
  gap: 20rem;

  width: 100%;
`;

const titleStyle = css`
  margin-bottom: 3rem;

  font-size: 4rem;
  font-weight: 700;
`;

export {
  closeModalButtonStyle,
  deleteButtonStyle,
  forgiveButtonStyle,
  headerStyle,
  layoutStyle,
  listBundleStyle,
  listSectionStyle,
  renameButtonStyle,
  renameFieldSetStyle,
  renameFormStyle,
  sectionStyle,
  subscriberListStyle,
  spaceBetweenStyle,
  titleStyle,
};
