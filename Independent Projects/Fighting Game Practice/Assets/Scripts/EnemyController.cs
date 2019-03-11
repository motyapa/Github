using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EnemyController : MonoBehaviour
{

    private int health;
    private Animator anim;
    private Rigidbody2D rb2d;
    private GameObject player;
    private GameObject enemy;  
    private int dir = -1;
    private float speed = 1f;

    private bool facingRight = false;
    private bool attacking = false;
    private bool hit = false;
    private bool dying = false;
    // Start is called before the first frame update
    void Start()
    {
        health = 7;
        anim = gameObject.GetComponent<Animator>();
        rb2d = gameObject.GetComponent<Rigidbody2D>();
        player = GameObject.FindWithTag("Player");
    }

    // Update is called once per frame
    void FixedUpdate()
    {
        if (!dying && !hit)
        {
            if (player.transform.position.x > gameObject.transform.position.x)
            {
                if (!facingRight)
                {
                    Flip();
                }
                rb2d.velocity = new Vector2(1 * speed, rb2d.velocity.y);
            }
            else if (player.transform.position.x < gameObject.transform.position.x)
            {
                if (facingRight)
                {
                    Flip();
                }
                rb2d.velocity = new Vector2(-1 * speed, rb2d.velocity.y);
            }

            if (Mathf.Abs(player.transform.position.x - gameObject.transform.position.x) < 1.6 && !attacking)
            {
                attacking = true;
                StartCoroutine(Attack());
            }
        }
    }

    void Damage(string name)
    {
        hit = true;
        int damage = 0;
        Vector2 v = new Vector2(0, 0);

        if (name == "Uppercut")
        {
            damage = 2;
            v = new Vector2(0, 35);
        }
        else if (name == "Punch")
        {
            damage = 1;
            v = new Vector2(-1 * dir * 35, 0);
        }

        health -= damage;
        if (health <= 0)
        {
            dying = true;
            StartCoroutine(Death());
        }
        else
        {
            StartCoroutine(Hit(v));
        }
    }

    private IEnumerator Attack()
    {
        anim.Play("Attack");
        RaycastHit2D hit = Physics2D.Raycast(transform.position, new Vector2(1, 0) * dir, 1.6f);

        if (hit.collider != null)
        {
            Debug.Log("Enemy hit player");

            //NTS: Damage changes based on attack
            hit.collider.SendMessage("Damage");
        }
        yield return new WaitForSeconds(anim.GetCurrentAnimatorStateInfo(0).length);
        attacking = false;
    }

    private IEnumerator Hit(Vector2 v)
    {
        rb2d.velocity = new Vector2(0, 0);
        rb2d.AddForce(v, ForceMode2D.Impulse);
        anim.Play("Hit");
        yield return new WaitForSeconds(.5f);
        hit = false;
    }
    private IEnumerator Death()
    {
        anim.Play("Death");
        //Note: the .05 is in the code because otherwise the idle animation seems to show up for a frame or two before Destroy.
        yield return new WaitForSeconds(anim.GetCurrentAnimatorStateInfo(0).length - (float).05);
        Destroy(gameObject);            
    }

    void Flip()
    {
        facingRight = !facingRight;
        Vector2 temp = gameObject.transform.localScale;
        temp.x *= -1;
        gameObject.transform.localScale = temp;
        dir *= -1;
    }
}
