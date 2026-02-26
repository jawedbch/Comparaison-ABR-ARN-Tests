import os
import pandas as pd
import matplotlib.pyplot as plt

CSV_PATH = os.path.join("target", "bench", "results.csv")
OUT_DIR = os.path.join("target", "bench", "plots")
os.makedirs(OUT_DIR, exist_ok=True)

df = pd.read_csv(CSV_PATH)

def plot_mode(mode):
    d = df[df["mode"] == mode].sort_values("n").copy()

    # ns -> ms
    d["build_abr_ms"] = d["build_abr_ns"] / 1e6
    d["build_arn_ms"] = d["build_arn_ns"] / 1e6
    d["search_abr_ms"] = d["search_abr_ns"] / 1e6
    d["search_arn_ms"] = d["search_arn_ns"] / 1e6

    x = d["n"]

    plt.figure()
    plt.plot(x, d["build_abr_ms"], label="ABR")
    plt.plot(x, d["build_arn_ms"], label="ARN")
    plt.xlabel("n")
    plt.ylabel("Temps (ms)")
    plt.title(f"Construction ({mode})")
    plt.grid(True)
    plt.legend()
    plt.savefig(os.path.join(OUT_DIR, f"construction_{mode}.png"), dpi=200)

    plt.figure()
    plt.plot(x, d["search_abr_ms"], label="ABR")
    plt.plot(x, d["search_arn_ms"], label="ARN")
    plt.xlabel("n")
    plt.ylabel("Temps (ms)")
    plt.title(f"Recherche 0..2n-1 ({mode})")
    plt.grid(True)
    plt.legend()
    plt.savefig(os.path.join(OUT_DIR, f"recherche_{mode}.png"), dpi=200)

plot_mode("random")
plot_mode("sorted")

print("Graphes générés dans :", OUT_DIR)

