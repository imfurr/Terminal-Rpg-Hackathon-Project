def main():
    with open("name.txt", "r") as file:
        name = file.read()
        print("___________________________________________________________________________________________________________________________________________________")
        print(f"\033[1mCongratulations, {name}! Your name shall be etched into the annals of history, for defeating every enemy and obtaining every treasure.\033[0m")
        print("___________________________________________________________________________________________________________________________________________________")
    with open("health.txt", "r") as file:
        health = file.read()
        print(f"\033[1mYou have ended this paramount journey with {health} Hitpoints remaining!\033[0m")
        print("___________________________________________________________________________________________________________________________________________________")
if __name__ == "__main__":
    main()