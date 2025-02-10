import styles from './NavBar.module.css';

function NavBar() {
    return (
        <nav className={styles.navbar}>
            <div className={styles['navbar-left']}>
                <ul className={styles['nav-links']}>
                    <li>
                        <a href="/">HOME</a>
                    </li>
                </ul>
            </div>
            <div className={styles['navbar-center']}>
                <ul className={styles['nav-links']}>
                    <li>
                        <a href="/">LEGISLATION</a>
                    </li>
                    <li>
                        <a href="/">SEE GOVERNMENT SPENDING</a>
                    </li>
                    <li>
                        <a href="/">FIND YOUR REPRESENTATIVES</a>
                    </li>
                </ul>
            </div>
            <div className={styles['navbar-right']}>
                <ul className={styles['nav-links']}>
                    <li>
                        <a href="/">CONNECT</a>
                    </li>
                </ul>
            </div>

        </nav>
    );
}

export default NavBar;
